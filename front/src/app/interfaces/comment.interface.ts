export interface Comment {
    id: number;
    message: string;
    user_id: number;
    article_id: number;
    createdAt: Date;
    updatedAt?: Date;
}
