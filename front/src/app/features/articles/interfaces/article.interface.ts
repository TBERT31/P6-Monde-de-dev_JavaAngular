export interface Article {
    id: number;
    title: string;
    content: string;
    author: string;
    topic_id: number;
    comments: number[];
    createdAt?: Date;
    updatedAt?: Date;
}